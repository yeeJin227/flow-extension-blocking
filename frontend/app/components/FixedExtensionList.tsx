'use client';

import { FixedExtension } from '@/app/types/extension';

interface FixedExtensionListProps {
  extensions: FixedExtension[];
  onToggle: (fixExtensionName: string, isCheck: boolean) => void;
}

export default function FixedExtensionList({
  extensions,
  onToggle,
}: FixedExtensionListProps) {
  return (
    <div className="flex flex-wrap gap-4">
      {extensions.map((extension) => (
        <label
          key={extension.fixExtensionName}
          className="flex items-center gap-2 cursor-pointer p-3 rounded-lg border border-gray-300 hover:bg-gray-50 transition-colors"
        >
          <input
            type="checkbox"
            checked={extension.isCheck}
            onChange={(e) => onToggle(extension.fixExtensionName, e.target.checked)}
            className="w-5 h-5 cursor-pointer"
          />
          <span className="text-gray-700 font-medium">
            {extension.fixExtensionName}
          </span>
        </label>
      ))}
    </div>
  );
}

