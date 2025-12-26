'use client';

import { useState, useEffect } from 'react';
import { FixedExtension, CustomExtension } from '@/app/types/extension';
import {
  getFixedExtensions,
  updateFixedExtension,
  getCustomExtensions,
  addCustomExtension,
  deleteCustomExtension,
} from '@/app/lib/api';
import FixedExtensionList from './FixedExtensionList';
import CustomExtensionInput from './CustomExtensionInput';

export default function ExtensionManager() {
  const [fixedExtensions, setFixedExtensions] = useState<FixedExtension[]>([]);
  const [customExtensions, setCustomExtensions] = useState<CustomExtension[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 초기 데이터 로드
  useEffect(() => {
    loadExtensions();
  }, []);

  const loadExtensions = async () => {
    try {
      setLoading(true);
      setError(null);
      const [fixed, custom] = await Promise.all([
        getFixedExtensions(),
        getCustomExtensions(),
      ]);
      setFixedExtensions(fixed);
      setCustomExtensions(custom);
    } catch (err) {
      setError(err instanceof Error ? err.message : '데이터 로드 실패');
    } finally {
      setLoading(false);
    }
  };

  // 고정 확장자 체크 상태 변경
  const handleFixedExtensionToggle = async (
    fixExtensionName: string,
    isCheck: boolean
  ) => {
    try {
      await updateFixedExtension(fixExtensionName, isCheck);
      setFixedExtensions((prev) =>
        prev.map((ext) =>
          ext.fixExtensionName === fixExtensionName
            ? { ...ext, isCheck }
            : ext
        )
      );
    } catch (err) {
      alert(err instanceof Error ? err.message : '업데이트 실패');
    }
  };

  // 커스텀 확장자 추가
  const handleAddCustomExtension = async (customExtensionName: string) => {
    try {
      // 중복 체크
      if (
        customExtensions.some(
          (ext) => ext.customExtensionName.toLowerCase() === customExtensionName.toLowerCase()
        )
      ) {
        alert('이미 존재하는 커스텀 확장자입니다.');
        return;
      }

      // 최대 개수 체크
      if (customExtensions.length >= 200) {
        alert('커스텀 확장자는 최대 200개까지 추가할 수 있습니다.');
        return;
      }

      const newExtension = await addCustomExtension(customExtensionName);
      setCustomExtensions((prev) => [...prev, newExtension]);
    } catch (err) {
      alert(err instanceof Error ? err.message : '추가 실패');
    }
  };

  // 커스텀 확장자 삭제
  const handleDeleteCustomExtension = async (customExtensionName: string) => {
    try {
      await deleteCustomExtension(customExtensionName);
      setCustomExtensions((prev) =>
        prev.filter((ext) => ext.customExtensionName !== customExtensionName)
      );
    } catch (err) {
      alert(err instanceof Error ? err.message : '삭제 실패');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-lg">로딩 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-red-500">오류: {error}</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4">
      <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-md p-8">
        <h1 className="text-2xl font-bold mb-8 text-gray-800">
          확장자 차단 관리
        </h1>

        {/* 고정 확장자 섹션 */}
        <div className="mb-8">
          <h2 className="text-lg font-semibold mb-4 text-gray-700">
            고정 확장자
          </h2>
          <FixedExtensionList
            extensions={fixedExtensions}
            onToggle={handleFixedExtensionToggle}
          />
        </div>

        {/* 커스텀 확장자 섹션 */}
        <div>
          <h2 className="text-lg font-semibold mb-4 text-gray-700">
            커스텀 확장자
          </h2>
          <CustomExtensionInput
            extensions={customExtensions}
            onAdd={handleAddCustomExtension}
            onDelete={handleDeleteCustomExtension}
          />
        </div>
      </div>
    </div>
  );
}

