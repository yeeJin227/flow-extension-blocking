import { FixedExtension, CustomExtension } from '@/app/types/extension';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// 고정 확장자 목록 조회
export async function getFixedExtensions(): Promise<FixedExtension[]> {
  const response = await fetch(`${API_BASE_URL}/extensions/fixed`);
  if (!response.ok) {
    throw new Error('고정 확장자 조회 실패');
  }
  return response.json();
}

// 고정 확장자 체크 상태 업데이트
export async function updateFixedExtension(
  fixExtensionName: string,
  isCheck: boolean
): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/extensions/fixed`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      fixExtensionName,
      isCheck,
    }),
  });
  if (!response.ok) {
    throw new Error('고정 확장자 업데이트 실패');
  }
}

// 커스텀 확장자 목록 조회
export async function getCustomExtensions(): Promise<CustomExtension[]> {
  const response = await fetch(`${API_BASE_URL}/extensions/custom`);
  if (!response.ok) {
    throw new Error('커스텀 확장자 조회 실패');
  }
  return response.json();
}

// 커스텀 확장자 추가
export async function addCustomExtension(
  customExtensionName: string
): Promise<CustomExtension> {
  const response = await fetch(`${API_BASE_URL}/extensions/custom`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      customExtensionName,
    }),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || '커스텀 확장자 추가 실패');
  }
  return response.json();
}

// 커스텀 확장자 삭제
export async function deleteCustomExtension(
  customExtensionName: string
): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/extensions/custom/${encodeURIComponent(customExtensionName)}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('커스텀 확장자 삭제 실패');
  }
}

